package io.tech1.framework.utilities.utils;

import io.tech1.framework.domain.http.requests.IPAddress;
import io.tech1.framework.domain.http.requests.UserAgentHeader;
import io.tech1.framework.domain.http.requests.UserRequestMetadata;

public interface UserMetadataUtils {
    UserRequestMetadata getUserRequestMetadataProcessed(IPAddress ipAddress, UserAgentHeader userAgentHeader);
}
