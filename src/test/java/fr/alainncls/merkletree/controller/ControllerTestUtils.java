package fr.alainncls.merkletree.controller;

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.restdocs.operation.preprocess.Preprocessors;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;

public class ControllerTestUtils {

    static OperationRequestPreprocessor preprocessRequest() {
        return Preprocessors.preprocessRequest(removeHeaders("Content-Length", "X-CSRF-TOKEN"), prettyPrint());
    }

    static OperationResponsePreprocessor preprocessResponse() {
        return Preprocessors.preprocessResponse(removeHeaders("Content-Length", "Pragma", "X-XSS-Protection", "Expires", "X-Frame-Options", "X-Content-Type-Options", "Cache-Control"), prettyPrint());
    }

}