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
package io.github.rednesto.fileinventories.api.data.mutable;

import io.github.rednesto.fileinventories.api.FileInvKeys;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInvSecondaryClickData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class OnInvSecondaryClickData extends AbstractSingleData<String, OnInvSecondaryClickData, ImmutableOnInvSecondaryClickData> {

    public OnInvSecondaryClickData() {
        super("", FileInvKeys.ON_INV_SECONDARY_CLICK);
    }

    public OnInvSecondaryClickData(String value) {
        super(value, FileInvKeys.ON_INV_SECONDARY_CLICK);
    }

    public Value<String> onInvSecondaryClick() {
        return Sponge.getRegistry().getValueFactory().createValue(FileInvKeys.ON_INV_SECONDARY_CLICK, getValue());
    }

    @Override
    protected Value<?> getValueGetter() {
        return onInvSecondaryClick();
    }

    @Override
    public Optional<OnInvSecondaryClickData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(OnInvSecondaryClickData.class).ifPresent(data -> setValue(overlap.merge(this, data).getValue()));

        return Optional.of(this);
    }

    @Override
    public Optional<OnInvSecondaryClickData> from(DataContainer container) {
        if(container.contains(FileInvKeys.ON_INV_SECONDARY_CLICK.getQuery()))
            return Optional.of(set(FileInvKeys.ON_INV_SECONDARY_CLICK, container.getString(FileInvKeys.ON_INV_SECONDARY_CLICK.getQuery()).orElse(getValue())));

        return Optional.empty();
    }

    @Override
    public OnInvSecondaryClickData copy() {
        return new OnInvSecondaryClickData(this.getValue());
    }

    @Override
    public ImmutableOnInvSecondaryClickData asImmutable() {
        return new ImmutableOnInvSecondaryClickData(this.getValue());
    }

    @Override
    public DataContainer toContainer() {
        return Sponge.getDataManager().createContainer().set(FileInvKeys.ON_INV_SECONDARY_CLICK, getValue());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
