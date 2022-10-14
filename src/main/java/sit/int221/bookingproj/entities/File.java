package sit.int221.bookingproj.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fileId", nullable = false)
    private Integer fileId;
    @Size(max = 100 , message = "length exceeded the size")
    @Column(name = "fileName")
    private String fileName;
    @Size(max = 100 , message = "length exceeded the size")
    @Column(name = "filePath")
    private String filePath;
    @Size(max = 50)
    @Column(name = "fileSize")
    private String fileSize;
}
