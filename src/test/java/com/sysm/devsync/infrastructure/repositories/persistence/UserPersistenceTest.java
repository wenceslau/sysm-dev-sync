package com.sysm.devsync.infrastructure.repositories.persistence;

import com.sysm.devsync.domain.BusinessException;
import com.sysm.devsync.domain.Page;
import com.sysm.devsync.domain.Pagination;
import com.sysm.devsync.domain.SearchQuery;
import com.sysm.devsync.domain.enums.UserRole;
import com.sysm.devsync.domain.models.User;
import com.sysm.devsync.infrastructure.AbstractRepositoryTest;
import com.sysm.devsync.infrastructure.repositories.entities.UserJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sysm.devsync.infrastructure.Utils.sleep;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Import(UserPersistence.class)
public class UserPersistenceTest extends AbstractRepositoryTest {

    @Autowired
    private UserPersistence userPersistence;

    private User user1Domain;

    private UserJpaEntity user1Jpa;
    private UserJpaEntity user2Jpa;
    private UserJpaEntity user3Jpa;

    @BeforeEach
    void setUp() {
        clearRepositories();

        user1Domain = User.create("John Doe", "john.doe@example.com", UserRole.MEMBER);
        User user2Domain = User.create("Alice Smith", "alice.smith@example.com", UserRole.ADMIN);
        User user3Domain = User.create("Bob Johnson", "bob.johnson@example.com", UserRole.MEMBER);

        user1Jpa = UserJpaEntity.fromModel(user1Domain);
        user2Jpa = UserJpaEntity.fromModel(user2Domain);
        user3Jpa = UserJpaEntity.fromModel(user3Domain);
    }

    @Nested
    @DisplayName("create Method Tests")
    class CreateTests {
        @Test
        @DisplayName("should create and save a user")
        void create_shouldSaveUser() {
            User newUser = User.create("Test User", "test.user@example.com", UserRole.MEMBER);
            // Act
            assertDoesNotThrow(() -> create(newUser));

            // Assert
            UserJpaEntity foundInDb = entityManager.find(UserJpaEntity.class, newUser.getId());
            assertThat(foundInDb).isNotNull();
            assertThat(foundInDb.getName()).isEqualTo(newUser.getName());
            assertThat(foundInDb.getEmail()).isEqualTo(newUser.getEmail());

            Optional<User> foundUser = userPersistence.findById(newUser.getId());
            assertThat(foundUser).isPresent();
            assertThat(foundUser.get().getName()).isEqualTo(newUser.getName());
            assertThat(foundUser.get().getEmail()).isEqualTo(newUser.getEmail());
            assertThat(foundUser.get().getRole()).isEqualTo(UserRole.MEMBER);
        }
    }

    @Nested
    @DisplayName("update Method Tests")
    class UpdateTests {
        @Test
        @DisplayName("should update an existing user")
        void update_shouldModifyExistingUser() {
            // Arrange
            entityPersist(user1Jpa);
            sleep(100);
            Instant originalCreatedAt = user1Domain.getCreatedAt();
            User updatedDomainUser = User.build(
                    user1Domain.getId(),
                    originalCreatedAt,
                    Instant.now(),
                    "Johnathan Doe Updated",
                    "john.doe.new@example.com",
                    "newPasswordHash",
                    "newProfilePic.jpg",
                    UserRole.ADMIN
            );

            // Act
            update(updatedDomainUser);

            // Assert
            Optional<User> foundUser = userPersistence.findById(user1Domain.getId());
            assertThat(foundUser).isPresent();
            User retrievedUser = foundUser.get();
            assertThat(retrievedUser.getName()).isEqualTo("Johnathan Doe Updated");
            assertThat(retrievedUser.getEmail()).isEqualTo("john.doe.new@example.com");
            assertThat(retrievedUser.getPasswordHash()).isEqualTo("newPasswordHash");
            assertThat(retrievedUser.getProfilePictureUrl()).isEqualTo("newProfilePic.jpg");
            assertThat(retrievedUser.getRole()).isEqualTo(UserRole.ADMIN);
            assertThat(retrievedUser.getCreatedAt()).isEqualTo(originalCreatedAt);
            assertThat(retrievedUser.getUpdatedAt()).isAfter(originalCreatedAt);
        }
    }

    @Nested
    @DisplayName("deleteById Method Tests")
    class DeleteByIdTests {
        @Test
        @DisplayName("should delete a user by its ID")
        void deleteById_shouldRemoveUser() {
            // Arrange
            entityPersist(user1Jpa);

            // Act
            deleteById(user1Jpa.getId());

            // Assert
            Optional<User> foundUser = userPersistence.findById(user1Jpa.getId());
            assertThat(foundUser).isNotPresent();
            assertThat(userPersistence.existsById(user1Jpa.getId())).isFalse();
            assertThat(entityManager.find(UserJpaEntity.class, user1Jpa.getId())).isNull();
        }
    }

    @Nested
    @DisplayName("findById Method Tests")
    class FindByIdTests {
        @Test
        @DisplayName("should return user when found")
        void findById_whenUserExists_shouldReturnUser() {
            // Arrange
            entityPersist(user1Jpa);

            // Act
            Optional<User> foundUser = userPersistence.findById(user1Domain.getId());

            // Assert
            assertThat(foundUser).isPresent();
            assertThat(foundUser.get().getId()).isEqualTo(user1Domain.getId());
            assertThat(foundUser.get().getName()).isEqualTo(user1Domain.getName());
        }

        @Test
        @DisplayName("should return empty optional when user not found")
        void findById_whenUserDoesNotExist_shouldReturnEmpty() {
            // Act
            Optional<User> foundUser = userPersistence.findById(UUID.randomUUID().toString());

            // Assert
            assertThat(foundUser).isNotPresent();
        }
    }

    @Nested
    @DisplayName("existsById Method Tests")
    class ExistsByIdTests {
        @Test
        @DisplayName("should return true when user exists")
        void existsById_whenUserExists_shouldReturnTrue() {
            // Arrange
            entityPersist(user1Jpa);

            // Act
            boolean exists = userPersistence.existsById(user1Domain.getId());

            // Assert
            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("should return false when user does not exist")
        void existsById_whenUserDoesNotExist_shouldReturnFalse() {
            // Act
            boolean exists = userPersistence.existsById(UUID.randomUUID().toString());

            // Assert
            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("findAll Method Tests")
    class FindAllTests {
        @BeforeEach
        void setUpFindAll() {
            entityPersist(user1Jpa); // John Doe, john.doe@example.com, MEMBER
            entityPersist(user2Jpa); // Alice Smith, alice.smith@example.com, ADMIN
            entityPersist(user3Jpa); // Bob Johnson, bob.johnson@example.com, MEMBER
        }

        @Test
        @DisplayName("should return all users when no search terms provided")
        void findAll_noTerms_shouldReturnAllUsers() {
            SearchQuery query = SearchQuery.of(Page.of(0, 10), Map.of());
            Pagination<User> result = userPersistence.findAll(query);
            assertThat(result.items()).hasSize(3);
            assertThat(result.total()).isEqualTo(3);
        }

        @Test
        @DisplayName("should filter by a single valid term (name)")
        void findAll_singleValidTermName_shouldReturnMatchingUsers() {
            SearchQuery query = SearchQuery.of(Page.of(0, 10), Map.of("name", "John Doe"));
            Pagination<User> result = userPersistence.findAll(query);
            assertThat(result.items()).hasSize(1);
            assertThat(result.items().get(0).getName()).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("should filter by a single valid term (email) case-insensitive")
        void findAll_singleValidTermEmail_shouldReturnMatchingUsers() {
            SearchQuery query = SearchQuery.of(Page.of(0, 10), Map.of("email", "ALICE.SMITH@EXAMPLE.COM"));
            Pagination<User> result = userPersistence.findAll(query);
            assertThat(result.items()).hasSize(1);
            assertThat(result.items().get(0).getEmail()).isEqualTo("alice.smith@example.com");
        }

        @Test
        @DisplayName("should filter by a single valid term (role)")
        void findAll_singleValidTermRole_shouldReturnMatchingUsers() {
            SearchQuery query = SearchQuery.of(Page.of(0, 10), Map.of("role", "MEMBER"));
            Pagination<User> result = userPersistence.findAll(query);
            assertThat(result.items()).hasSize(2);
            assertThat(result.items()).extracting(User::getRole).containsOnly(UserRole.MEMBER);
        }

        @Test
        @DisplayName("should filter by multiple valid terms (AND logic)")
        void findAll_withMultipleTerms_shouldReturnAndedResults() {
            // Arrange: Search for a user who is a MEMBER and whose name is "John Doe"
            SearchQuery queryWithMatch = SearchQuery.of(Page.of(0, 10), Map.of("role", "MEMBER", "name", "John Doe"));

            // Act
            Pagination<User> resultWithMatch = userPersistence.findAll(queryWithMatch);

            // Assert: Should find exactly one user
            assertThat(resultWithMatch.items()).hasSize(1);
            assertThat(resultWithMatch.items().get(0).getName()).isEqualTo("John Doe");
            assertThat(resultWithMatch.total()).isEqualTo(1);

            // Arrange: Search for a user who is an ADMIN and whose name is "John Doe" (should not exist)
            SearchQuery queryWithoutMatch = SearchQuery.of(Page.of(0, 10), Map.of("role", "ADMIN", "name", "John Doe"));

            // Act
            Pagination<User> resultWithoutMatch = userPersistence.findAll(queryWithoutMatch);

            // Assert: Should find no users
            assertThat(resultWithoutMatch.items()).isEmpty();
            assertThat(resultWithoutMatch.total()).isZero();
        }

        @Test
        @DisplayName("should throw BusinessException for an invalid search field")
        void findAll_invalidSearchField_shouldThrowBusinessException() {
            SearchQuery query = SearchQuery.of(Page.of(0, 10), Map.of("nonExistentField", "value"));
            assertThatThrownBy(() -> userPersistence.findAll(query))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Invalid search field provided: 'nonExistentField'");
        }

        @Test
        @DisplayName("should throw BusinessException for an invalid role value")
        void findAll_invalidRoleValue_shouldThrowBusinessException() {
            SearchQuery query = SearchQuery.of(Page.of(0, 10), Map.of("role", "GUEST"));
            assertThatThrownBy(() -> userPersistence.findAll(query))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("Invalid value for role field: 'GUEST'");
        }

        @Test
        @DisplayName("should handle terms with no matches")
        void findAll_termWithNoMatches_shouldReturnEmptyPage() {
            SearchQuery query = SearchQuery.of(Page.of(0, 10), Map.of("name", "NonExistent User"));
            Pagination<User> result = userPersistence.findAll(query);
            assertThat(result.items()).isEmpty();
            assertThat(result.total()).isZero();
        }

        @Test
        @DisplayName("should respect pagination and sorting parameters")
        void findAll_withPaginationAndSorting_shouldReturnCorrectPage() {
            // Sort by name ascending
            SearchQuery query = SearchQuery.of(Page.of(0, 2, "name", "asc"), Map.of());
            Pagination<User> result = userPersistence.findAll(query);

            assertThat(result.items()).hasSize(2);
            assertThat(result.currentPage()).isEqualTo(0);
            assertThat(result.total()).isEqualTo(3);
            assertThat(result.items().get(0).getName()).isEqualTo("Alice Smith"); // A comes before B
            assertThat(result.items().get(1).getName()).isEqualTo("Bob Johnson");

            // Get the next page
            SearchQuery queryPage2 = SearchQuery.of(Page.of(1, 2, "name", "asc"), Map.of());
            Pagination<User> result2 = userPersistence.findAll(queryPage2);
            assertThat(result2.items()).hasSize(1);
            assertThat(result2.items().get(0).getName()).isEqualTo("John Doe");
        }
    }

    private void create(User entity) {
        userPersistence.create(entity);
        flushAndClear();
    }

    private void update(User entity) {
        userPersistence.update(entity);
        flushAndClear();
    }

    private void deleteById(String id) {
        userPersistence.deleteById(id);
        flushAndClear();
    }
}
