/**
 *  Copyright (c) 2018 Uber Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uber.rxcentralble;

import io.reactivex.ObservableTransformer;

/** Provides the capability to specify logic to identify discovered peripheral matches. */
public interface ScanMatcher {

  /**
   * Transform a stream of discovered peripherals into the desired matches.
   */
  ObservableTransformer<ScanData, ScanData> match();
}
