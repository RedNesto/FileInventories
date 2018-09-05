/*
 * MIT License
 *
 * Copyright (c) 2017 RedNesto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.rednesto.fileinventories.api.data.immutable;

import io.github.rednesto.fileinventories.api.FileInvKeys;
import io.github.rednesto.fileinventories.api.data.mutable.OnInteractPrimaryClickData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableOnInteractPrimaryClickData extends AbstractImmutableSingleData<String, ImmutableOnInteractPrimaryClickData, OnInteractPrimaryClickData> {

    public ImmutableOnInteractPrimaryClickData(String value) {
        super(value, FileInvKeys.ON_INTERACT_PRIMARY_CLICK);
    }

    @Override
    protected ImmutableValue<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory()
                .createValue(FileInvKeys.ON_INTERACT_PRIMARY_CLICK, getValue())
                .asImmutable();
    }

    @Override
    public OnInteractPrimaryClickData asMutable() {
        return new OnInteractPrimaryClickData(this.getValue());
    }

    @Override
    public DataContainer toContainer() {
        return Sponge.getDataManager().createContainer().set(FileInvKeys.ON_INTERACT_PRIMARY_CLICK, getValue());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
