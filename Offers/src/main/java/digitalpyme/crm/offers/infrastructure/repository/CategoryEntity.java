package digitalpyme.crm.offers.infrastructure.repository;

import jakarta.persistence.*;
import lombok.*;
import digitalpyme.crm.offers.domain.Category;

@Entity(name = "Category")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryEntity implements DomainTranslatable<Category> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategory;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = true, length = 255)
    private String description;

    @Column(name = "parentId", nullable = true)
    private Long parentId;

    public static CategoryEntity fromDomain(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryEntity.builder()
                .idCategory(category.getIdCategory())
                .name(category.getName())
                .description(category.getDescription())
                .parentId(category.getParentId())
                .build();
    }

    @Override
    public Category toDomain() {
        return Category.builder()
                .idCategory(this.getIdCategory())
                .name(this.getName())
                .description(this.getDescription())
                .parentId(this.getParentId())
                .build();
    }
}
