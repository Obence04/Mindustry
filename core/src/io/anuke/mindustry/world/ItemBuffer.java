package io.anuke.mindustry.world;

import io.anuke.arc.util.*;
import io.anuke.mindustry.type.Item;

import static io.anuke.mindustry.Vars.content;

public class ItemBuffer{
    private final float speed;

    private long[] buffer;
    private int index;

    public ItemBuffer(int capacity, float speed){
        this.buffer = new long[capacity];
        this.speed = speed;
    }

    public boolean accepts(){
        return index < buffer.length;
    }

    public void accept(Item item, short data){
        //if(!accepts()) return;
        buffer[index++] = Pack.longInt(Float.floatToIntBits(Time.time()), Pack.shortInt((short)item.id, data));
    }

    public void accept(Item item){
        accept(item, (short)-1);
    }

    public Item poll(){
        if(index > 0){
            long l = buffer[0];
            float time = Float.intBitsToFloat(Pack.leftInt(l));

            if(Time.time() >= time + speed || Time.time() < time){
                return content.item(Pack.leftShort(Pack.rightInt(l)));
            }
        }
        return null;
    }

    public short pollData(){
        if(index > 0){
            long l = buffer[0];
            float time = Float.intBitsToFloat(Pack.leftInt(l));

            if(Time.time() >= time + speed || Time.time() < time){
                return Pack.rightShort(Pack.rightInt(l));
            }
        }
        return -1;
    }

    public void remove(){
        System.arraycopy(buffer, 1, buffer, 0, index - 1);
        index--;
    }
}
