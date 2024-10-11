package co.edu.uniquindio.UniEventos;

import co.edu.uniquindio.UniEventos.servicios.interfaces.ImagenesServicio;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ImagenesTest {
    @Autowired
    ImagenesServicio imagenesServicio;

    @Test
    public void testUploadImage() throws Exception {
        // Cargar un archivo de imagen de prueba desde los recursos del proyecto
        ClassPathResource imgFile = new ClassPathResource("imgs/img.png");
        InputStream inputStream = new FileInputStream(imgFile.getFile());
        // Crear un MultipartFile simulado (Mock) para ser usado como parámetro en el servicio
        MultipartFile multipartFile = new MockMultipartFile(
                "file",                            // Nombre del archivo
                imgFile.getFilename(),              // Nombre del archivo original
                "image/jpeg",                       // Tipo MIME de la imagen
                inputStream                         // InputStream de la imagen
        );
        // Ejecutar el método de carga de imagen en el servicio
        String url = imagenesServicio.subirImagen(multipartFile);
        // Verificar que la URL de la imagen subida no sea nula
        assertNotNull(url);
        // Imprimir la URL de la imagen subida
        System.out.println("URL de la imagen subida: " + url);
    }

    // Prueba para la eliminación de una imagen
    @Test
    public void testDeleteImage() throws Exception {
        // Suponiendo que ya se subió una imagen y conocemos su nombre en el bucket de Firebase
        String nombreImagen = "bed9e096-e58d-4875-9fe9-d43f32f1e5a0-img.png"; // Nombre de la imagen en Firebase
        // Verificar que la imagen existe en el bucket antes de eliminarla
        Bucket bucket = StorageClient.getInstance().bucket();  // Obtener el bucket de Firebase
        Blob blob = bucket.get(nombreImagen);                  // Obtener el blob (la imagen) del bucket
        Assertions.assertNotNull(blob, "La imagen debería existir antes de ser eliminada");
        // Llamar al servicio para eliminar la imagen
        imagenesServicio.eliminarImagen(nombreImagen);
        // Verificar que la imagen ha sido eliminada correctamente del bucket
        Blob deletedBlob = bucket.get(nombreImagen);  // Intentar obtener la imagen nuevamente
        Assertions.assertNull(deletedBlob, "La imagen debería haber sido eliminada");
    }


}
