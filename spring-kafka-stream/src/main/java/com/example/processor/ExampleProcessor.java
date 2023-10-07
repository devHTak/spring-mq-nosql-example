package com.example.processor;

import org.springframework.cloud.stream.annotation.Input;

public interface ExampleProcessor {
    String INPUT1 = "exam-input1";
    String INPUT2 = "exam-input2";
    String OUTPUT = "exam-output";

    @Input

}
