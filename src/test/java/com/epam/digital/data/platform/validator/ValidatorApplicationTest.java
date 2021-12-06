/*
 * Copyright 2021 EPAM Systems.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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