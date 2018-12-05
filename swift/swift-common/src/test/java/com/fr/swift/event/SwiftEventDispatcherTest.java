package com.fr.swift.event;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author anchore
 * @date 12/4/2018
 */
public class SwiftEventDispatcherTest {

    @Test
    public void testListenAndFire() {
        SwiftEventDispatcher.listen(Evt.EVT1, new SwiftEventListener<Object>() {
            @Override
            public void on(Object data) {
                Assert.assertEquals("data1", data);
            }
        });

        SwiftEventDispatcher.fire(Evt.EVT1, "data1");

        SwiftEventDispatcher.listen(Evt.EVT2, new SwiftEventListener<Object>() {
            @Override
            public void on(Object data) {
                Assert.assertEquals("data2", data);
            }
        });
        SwiftEventDispatcher.fire(Evt.EVT1, "data1");
        SwiftEventDispatcher.fire(Evt.EVT2, "data2");
    }

    @Test
    public void testFireBlackHole() {
        SwiftEventDispatcher.fire(Evt.EVT1);
    }

    enum Evt implements SwiftEvent {
        EVT1, EVT2
    }
}