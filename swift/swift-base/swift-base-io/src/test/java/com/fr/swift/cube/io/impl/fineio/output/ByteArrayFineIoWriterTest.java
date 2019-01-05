package com.fr.swift.cube.io.impl.fineio.output;

import com.fr.swift.cube.io.impl.fineio.input.LongFineIoReader;
import com.fr.swift.cube.io.input.LongReader;
import com.fr.swift.cube.io.output.ByteWriter;
import com.fr.swift.cube.io.output.IntWriter;
import com.fr.swift.cube.io.output.LongWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URI;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;


/**
 * @author anchore
 * @date 2019/1/4
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ByteFineIoWriter.class, IntFineIoWriter.class, LongFineIoWriter.class, LongFineIoReader.class})
public class ByteArrayFineIoWriterTest {

    private final URI location = URI.create("/cubes/table/seg0/column/detail");

    private ByteWriter byteWriter = mock(ByteWriter.class);

    private IntWriter intWriter = mock(IntWriter.class);

    private LongWriter longWriter = mock(LongWriter.class);

    @Before
    public void setUp() throws Exception {
        mockStatic(ByteFineIoWriter.class);
        when(ByteFineIoWriter.build(Matchers.<URI>any(), anyBoolean())).thenReturn(byteWriter);

        mockStatic(IntFineIoWriter.class);
        when(IntFineIoWriter.build(Matchers.<URI>any(), anyBoolean())).thenReturn(intWriter);

        mockStatic(LongFineIoWriter.class);
        when(LongFineIoWriter.build(Matchers.<URI>any(), anyBoolean())).thenReturn(longWriter);


        mockStatic(LongFineIoReader.class);
        LongReader longReader = mock(LongReader.class);
        when(LongFineIoReader.build(Matchers.<URI>any())).thenReturn(longReader);
        when(longReader.isReadable()).thenReturn(true);
        when(longReader.get(0)).thenReturn(1L);
    }

    @Test
    public void build() {
        ByteArrayFineIoWriter.build(location, true);
        ByteArrayFineIoWriter.build(location, false);
    }

    @Test
    public void put() {
        ByteArrayFineIoWriter.build(location, true).put(0, new byte[]{1, 2, 3});

        verify(longWriter).put(anyLong(), anyLong());
        verify(intWriter).put(anyLong(), anyInt());
        verify(byteWriter, times(3)).put(anyLong(), anyByte());
        
        ByteArrayFineIoWriter.build(location, true).put(0, null);

        verify(longWriter, times(2)).put(anyLong(), anyLong());
        verify(intWriter, times(2)).put(anyLong(), anyInt());
        verifyNoMoreInteractions(byteWriter);
    }

    @Test
    public void release() {
        ByteArrayFineIoWriter.build(location, true).release();

        verify(byteWriter).release();
        verify(intWriter).release();
        verify(longWriter, times(2)).release();
    }
}