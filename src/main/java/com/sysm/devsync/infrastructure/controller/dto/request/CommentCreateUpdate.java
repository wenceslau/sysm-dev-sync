package com.sysm.devsync.infrastructure.controller.dto.request;

import com.sysm.devsync.domain.enums.TargetType;

public record CommentCreateUpdate(
        TargetType targetType,
        String targetId,
        String content
) {
}
