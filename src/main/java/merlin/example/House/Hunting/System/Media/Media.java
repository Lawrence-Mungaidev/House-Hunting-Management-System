package merlin.example.House.Hunting.System.Media;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import merlin.example.House.Hunting.System.ApartmentUnit.ApartmentUnit;

@Entity
@Getter
@Setter
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String Url;
    @Enumerated(EnumType.STRING)
    private MediaType mediaType;
    private String caption;

    @ManyToOne
    @JoinColumn(
            name = "apartmentUnitId"
    )
    @JsonBackReference
    private ApartmentUnit apartmentUnit;

    public Media(String url, MediaType mediaType, String caption) {
        Url = url;
        this.mediaType = mediaType;
        this.caption = caption;
    }
}
