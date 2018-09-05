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
package io.github.rednesto.fileinventories.api.data.builder;

import io.github.rednesto.fileinventories.api.FileInvKeys;
import io.github.rednesto.fileinventories.api.data.immutable.ImmutableOnInvPrimaryClickData;
import io.github.rednesto.fileinventories.api.data.mutable.OnInvPrimaryClickData;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.Optional;

public class OnInvPrimaryClickDataManipulatorBuilder extends AbstractDataBuilder<OnInvPrimaryClickData> implements DataManipulatorBuilder<OnInvPrimaryClickData, ImmutableOnInvPrimaryClickData> {

    public OnInvPrimaryClickDataManipulatorBuilder() {
        super(OnInvPrimaryClickData.class, 0);
    }

    @Override
    public OnInvPrimaryClickData create() {
        return new OnInvPrimaryClickData();
    }

    @Override
    public Optional<OnInvPrimaryClickData> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<OnInvPrimaryClickData> buildContent(DataView container) throws InvalidDataException {
        if(container.contains(FileInvKeys.ON_INV_PRIMARY_CLICK.getQuery()))
            return Optional.of(new OnInvPrimaryClickData(container.getString(FileInvKeys.ON_INV_PRIMARY_CLICK.getQuery()).orElse("")));

        return Optional.empty();
    }
}
