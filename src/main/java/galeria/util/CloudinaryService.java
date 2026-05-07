package galeria.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.util.Map;

public class CloudinaryService {

    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "dflhvqcrs", "dflhvqcrs",
            "949382936286443", "949382936286443",
            "Zf3xSAhL35DXqYZfsOoutQJVUeY", "Zf3xSAhL35DXqYZfsOoutQJVUeY",
            "secure", true
    ));

    // En CloudinaryService.java verifica que termine así:
    public static String subirImagen(File file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("secure_url");
            System.out.println("URL generada: " + url); // Agrega este print para depurar
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}