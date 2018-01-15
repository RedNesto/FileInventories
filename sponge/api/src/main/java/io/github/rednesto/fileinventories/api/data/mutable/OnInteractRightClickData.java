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
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInteractRightClickData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class OnInteractRightClickData extends AbstractSingleData<String, OnInteractRightClickData, ImmutableOnInteractRightClickData> {

    public OnInteractRightClickData() {
        super("", FileInvKeys.ON_INTERACT_RIGHT_CLICK);
    }

    public OnInteractRightClickData(String value) {
        super(value, FileInvKeys.ON_INTERACT_RIGHT_CLICK);
    }

    public Value<String> onRightClick() {
        return Sponge.getRegistry().getValueFactory().createValue(FileInvKeys.ON_INTERACT_RIGHT_CLICK, getValue());
    }

    @Override
    protected Value<?> getValueGetter() {
        return onRightClick();
    }

    @Override
    public Optional<OnInteractRightClickData> fill(DataHolder dataHolder, MergeFunction overlap) {
        dataHolder.get(OnInteractRightClickData.class).ifPresent(data -> setValue(overlap.merge(this, data).getValue()));

        return Optional.of(this);
    }

    @Override
    public Optional<OnInteractRightClickData> from(DataContainer container) {
        if(container.contains(FileInvKeys.ON_INTERACT_RIGHT_CLICK.getQuery()))
            return Optional.of(set(FileInvKeys.ON_INTERACT_RIGHT_CLICK, container.getString(FileInvKeys.ON_INTERACT_RIGHT_CLICK.getQuery()).orElse(getValue())));

        return Optional.empty();
    }

    @Override
    public OnInteractRightClickData copy() {
        return new OnInteractRightClickData(this.getValue());
    }

    @Override
    public ImmutableOnInteractRightClickData asImmutable() {
        return new ImmutableOnInteractRightClickData(this.getValue());
    }

    @Override
    public DataContainer toContainer() {
        return Sponge.getDataManager().createContainer().set(FileInvKeys.ON_INTERACT_RIGHT_CLICK, getValue());
    }

    @Override
    public int getContentVersion() {
        return 0;
    }
}
