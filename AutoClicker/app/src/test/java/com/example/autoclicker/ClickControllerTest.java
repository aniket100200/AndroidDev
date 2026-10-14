package com.example.autoclicker;

import com.example.autoclicker.click.ClickConfig;
import com.example.autoclicker.click.ClickController;
import com.example.autoclicker.click.ClickState;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class ClickControllerTest {

    private ClickController controller;

    @Before
    public void setUp() {
        controller = new ClickController();
    }

    @Test
    public void initialState_isIdle() {
        assertEquals(ClickState.IDLE, controller.getState());
        assertEquals(0, controller.getCompletedCount());
    }

    @Test
    public void start_withNullService_setsStateToError() {
        ClickConfig config = new ClickConfig(10, 200, 100f, 100f);
        AtomicReference<ClickState> lastState = new AtomicReference<>();
        AtomicReference<String> lastMessage = new AtomicReference<>();

        controller.start(config, null, new ClickController.ClickCallback() {
            @Override
            public void onStateChanged(ClickState state, String message) {
                lastState.set(state);
                lastMessage.set(message);
            }

            @Override
            public void onProgress(int completed, int total) {
            }
        });

        assertEquals(ClickState.ERROR, controller.getState());
        assertEquals(ClickState.ERROR, lastState.get());
        assertEquals("Accessibility service is unavailable.", lastMessage.get());
    }

    @Test
    public void stop_whenIdle_staysIdle() {
        controller.stop();
        assertEquals(ClickState.IDLE, controller.getState());
    }
}
