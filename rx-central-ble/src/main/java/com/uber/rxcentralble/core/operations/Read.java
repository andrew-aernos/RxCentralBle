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
package com.uber.rxcentralble.core.operations;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.Relay;
import com.uber.rxcentralble.Peripheral;
import com.uber.rxcentralble.PeripheralOperation;
import com.uber.rxcentralble.Optional;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

/** Read data from a Gatt characteristic. */
public class Read implements PeripheralOperation<byte[]> {

  private final Relay<Optional<Peripheral>> peripheralRelay = BehaviorRelay.createDefault(Optional.empty());
  private final Single<byte[]> resultSingle;

  public Read(UUID svc, UUID chr, int timeoutMs) {
    resultSingle =
        peripheralRelay
            .filter(Optional::isPresent)
            .map(Optional::get)
            .firstOrError()
            .doOnSuccess(g -> peripheralRelay.accept(Optional.empty()))
            .flatMap(peripheral -> peripheral.read(svc, chr))
            .toObservable()
            .share()
            .firstOrError()
            .timeout(timeoutMs, TimeUnit.MILLISECONDS);
  }

  @Override
  public final Single<byte[]> result() {
    return resultSingle;
  }

  @Override
  public void execute(Peripheral peripheral) {
    peripheralRelay.accept(Optional.of(peripheral));
  }

  @Override
  public Single<byte[]> executeWithResult(Peripheral peripheral) {
    return resultSingle
            .doOnSubscribe(disposable -> execute(peripheral));
  }
}
