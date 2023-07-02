package com.kakaobank.searchapi.config;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        String body = response.body().toString();

        /*if (status == 400) {
            return new FeignException.BadRequest(response.reason(), response.request());
        }
        return new BadRequest(message, request, body);
        case 401:
        return new Unauthorized(message, request, body);
        case 403:
        return new Forbidden(message, request, body);
        case 404:
        return new NotFound(message, request, body);
        case 405:
        return new MethodNotAllowed(message, request, body);

        return null;*/
        return null;
    }
}
