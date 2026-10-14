package com.example.autoclicker;

import com.example.autoclicker.click.ClickConfig;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ClickConfigTest {

    @Test
    public void validConfig_returnsNullError() {
        String result = ClickConfig.validate(20, 500, 500, 1000, 1080, 1920);
        assertNull(result);
    }

    @Test
    public void tapCountBelowMin_returnsError() {
        String result = ClickConfig.validate(0, 500, 500, 1000, 1080, 1920);
        assertNotNull(result);
    }

    @Test
    public void tapCountAboveMax_returnsError() {
        String result = ClickConfig.validate(101, 500, 500, 1000, 1080, 1920);
        assertNotNull(result);
    }

    @Test
    public void intervalBelowMin_returnsError() {
        String result = ClickConfig.validate(20, 100, 500, 1000, 1080, 1920);
        assertNotNull(result);
    }

    @Test
    public void intervalAboveMax_returnsError() {
        String result = ClickConfig.validate(20, 6000, 500, 1000, 1080, 1920);
        assertNotNull(result);
    }

    @Test
    public void negativeXCoordinate_returnsError() {
        String result = ClickConfig.validate(20, 500, -10, 1000, 1080, 1920);
        assertNotNull(result);
    }

    @Test
    public void xCoordinateExceedsBounds_returnsError() {
        String result = ClickConfig.validate(20, 500, 2000, 1000, 1080, 1920);
        assertNotNull(result);
    }

    @Test
    public void getters_returnCorrectValues() {
        ClickConfig config = new ClickConfig(20, 500, 300f, 400f);
        assertEquals(20, config.getTapCount());
        assertEquals(500, config.getIntervalMs());
        assertEquals(300f, config.getX(), 0.001f);
        assertEquals(400f, config.getY(), 0.001f);
    }
}
