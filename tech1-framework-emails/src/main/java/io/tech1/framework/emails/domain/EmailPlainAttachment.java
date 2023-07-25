package io.tech1.framework.emails.domain;

import java.util.Set;

public record EmailPlainAttachment(
        Set<String> to,
        String subject,
        String message,
        String attachmentFileName,
        String attachmentMessage
) {
}
