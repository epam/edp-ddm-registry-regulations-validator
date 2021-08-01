package com.epam.digital.data.platform.validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.epam.digital.data.platform.validator.rulebooks.RulesValidator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ValidatorApplicationTest {

  private ValidatorApplication application;

  @Mock
  RulesValidator firstValidator;
  @Mock
  RulesValidator secondValidator;

  @BeforeEach
  void init() {
    application = new ValidatorApplication(List.of(firstValidator, secondValidator));
  }

  @Test
  void shouldCallAllValidators() {
    application.run();

    verify(firstValidator).validate(any());
    verify(secondValidator).validate(any());
  }
}