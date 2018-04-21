package com.github.shynixn.petblocks.api.sponge.event;

import com.github.shynixn.petblocks.api.business.entity.PetBlock;

/**
 * PetBlock event which gets called when the player starts or stops riding his petblock.
 * <p>
 * Version 1.1
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2017 by Shynixn
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class PetBlockRideEvent extends PetBlockCancelAbleEvent {
    private final boolean isRiding;

    /**
     * Initializes a new petblock event.
     *
     * @param petBlock petblock
     * @param isRiding isRiding
     */
    public PetBlockRideEvent(PetBlock petBlock, boolean isRiding) {
        super(petBlock);
        this.isRiding = isRiding;
    }

    /**
     * Returns if the owner is currently riding the petblock.
     *
     * @return isRiding
     */
    public boolean isRiding() {
        return this.isRiding;
    }
}
