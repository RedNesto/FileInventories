/*
 * MIT License
 *
 * Copyright (c) 2017
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
package io.github.rednesto.fileinventories.api.data.mutable;

import io.github.rednesto.fileinventories.api.FileInvKeys;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInvLeftClickData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class OnInvLeftClickData extends AbstractSingleData<String, OnInvLeftClickData, ImmutableOnInvLeftClickData> {

    public OnInvLeftClickData() {
        super("", FileInvKeys.ON_INV_LEFT_CLICK);
    }

    public OnInvLeftClickData(String value) {
        super(value, FileInvKeys.ON_INV_LEFT_CLICK);
    }

    public Value<String> onInvLeftClick() {
        return Sponge.getRegistry().getValueFactory().createValue(FileInvKeys.ON_INV_LEFT_CLICK, getValue());
    }

    @Override
    protected Value<?> getValueGetter() {
        return onInvLeftClick();
    }

    @Override
    public Optional<OnInvLeftClickData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(OnInvLeftClickData.class).ifPresent(data -> setValue(overlap.merge(this, data).getValue()));

        return Optional.of(this);
    }

    @Override
    public Optional<OnInvLeftClickData> from(DataContainer container) {
        if(container.contains(FileInvKeys.ON_INV_LEFT_CLICK.getQuery()))
            return Optional.of(set(FileInvKeys.ON_INV_LEFT_CLICK, container.getString(FileInvKeys.ON_INV_LEFT_CLICK.getQuery()).orElse(getValue())));

        return Optional.empty();
    }

    @Override
    public OnInvLeftClickData copy() {
        return new OnInvLeftClickData(this.getValue());
    }

    @Override
    public ImmutableOnInvLeftClickData asImmutable() {
        return new ImmutableOnInvLeftClickData(this.getValue());
    }

    @Override
    public DataContainer toContainer() {
        return Sponge.getDataManager().createContainer().set(FileInvKeys.ON_INV_LEFT_CLICK, getValue());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
