package com.gawdski.chip8bygawdski.internal;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Anna Gawda
 * 19.03.2018
 */
@Test
class OpcodeResolverTest {
    private OpcodeResolver opcodeResolver;

    @BeforeClass
    private void setUp() {
        this.opcodeResolver = new OpcodeResolver();
    }

    @Test
    public void shouldReturnAND() {
        //given

        //when
//        opcodeResolver.resolve(0x0000);
        //then

    }

}