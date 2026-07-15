package com.prosperapp.util;

public class TestPasswordUtil {

    public static void main(String[] args) {
        String contrasenaOriginal = "miClaveSecreta123";

        System.out.println("=== Hasheando contraseña ===");
        String hash = PasswordUtil.hashear(contrasenaOriginal);
        System.out.println("Contraseña original: " + contrasenaOriginal);
        System.out.println("Hash generado: " + hash);

        System.out.println("\n=== Verificando con contraseña correcta ===");
        boolean correcta = PasswordUtil.verificar("miClaveSecreta123", hash);
        System.out.println("¿Coincide?: " + correcta);

        System.out.println("\n=== Verificando con contraseña incorrecta ===");
        boolean incorrecta = PasswordUtil.verificar("claveEquivocada", hash);
        System.out.println("¿Coincide?: " + incorrecta);

        System.out.println("\n=== Hasheando la misma contraseña otra vez ===");
        String hash2 = PasswordUtil.hashear(contrasenaOriginal);
        System.out.println("Hash 1: " + hash);
        System.out.println("Hash 2: " + hash2);
        System.out.println("¿Son iguales los hashes?: " + hash.equals(hash2) + " (debe ser false, es esperado)");
        System.out.println("¿Pero ambos verifican la misma contraseña?: " + PasswordUtil.verificar(contrasenaOriginal, hash2));
    }
}