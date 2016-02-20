package me.iamguus.ovo.utils;

import net.minecraft.server.v1_8_R2.*;
import org.bukkit.craftbukkit.v1_8_R2.CraftWorld;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Guus on 2-1-2016.
 */
public class CustomSheep extends EntitySheep {

    public CustomSheep(org.bukkit.World world) {
        super(((CraftWorld)world).getHandle());
    }

    @Override
    public void g(double d0, double d1, double d2) {

    }

    @Override
    public void move(double d0, double d1, double d2) {

    }

    @Override
    public void setOnFire(int i) {

    }

    @Override
    public void makeSound(String s, float f, float f1) {

    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        return false;
    }

    public static Object getPrivateField(String fieldName, Class clazz, Object object) {
        Field field;
        Object o = null;

        try {
            field = clazz.getDeclaredField(fieldName);

            field.setAccessible(true);

            o = field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return o;
    }
}
