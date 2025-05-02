package opensuite.crm.offers.application.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.offers.application.rest.request.CreateCategoryRequest;
import opensuite.crm.offers.domain.Category;
import opensuite.crm.offers.domain.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/category")
public class CategoryRESTController {

    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("getAllCategory");

        return categoryService.findAllCategories().stream().toList();
    }

    @GetMapping("/{idCategory}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Category> findCategoryById(@PathVariable @NotNull Long idCategory) {
        log.info("findCategoryById");

        return categoryService.findCategoryById(idCategory).map(category -> ResponseEntity.ok().body(category))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryRequest createCategoryRequest) {
        log.trace("createCategory");

        Category newCategory = new Category();
        if (createCategoryRequest.getParentId() != null) {
            Optional<Category> existingCategory = categoryService.findCategoryByName(createCategoryRequest.getName());

            if (existingCategory.isPresent()) {
                log.trace("Category exist " + existingCategory);
                return ResponseEntity.status(409).body("Category Already Exists.");
            }

            Optional<Category> categoryParent = categoryService.findCategoryById(createCategoryRequest.getParentId());
            if (categoryParent.isEmpty()) {
                log.trace("Category Parent not exist " + createCategoryRequest);
                return ResponseEntity.status(409).body("Category Parent not Exists.");
            }

            if (categoryParent.get().getParentId() != null){
                log.trace("Category Parent is subcategory " + createCategoryRequest);
                return ResponseEntity.status(409).body("The parent category is a subcategory. You cannot create more levels.");
            }

            newCategory.setParentId(createCategoryRequest.getParentId());
        }

        newCategory.setName(createCategoryRequest.getName());
        newCategory.setDescription(createCategoryRequest.getDescription());

        log.trace("Creating Category " + createCategoryRequest);

        Long categoryId = categoryService.createCategory(newCategory);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryId)
                .toUri();
        return ResponseEntity.created(uri).body(categoryId);
    }


    @PutMapping("/{idCategory}")
    public ResponseEntity<?> updateCategory(@RequestBody @Valid CreateCategoryRequest updateCategoryRequest, @PathVariable Long idCategory) {
        log.trace("updateCategory");

        Optional<Category> existingCategory = categoryService.findCategoryById(idCategory);

        if (existingCategory.isEmpty()) {
            log.trace("Category not exist " + updateCategoryRequest);
            return ResponseEntity.status(409).body("Category not Exists.");
        }
        if (updateCategoryRequest.getParentId() != null) {

            if (idCategory == updateCategoryRequest.getParentId()) {
                log.trace("Category Parent and idCategory is same " + updateCategoryRequest);
                return ResponseEntity.status(409).body("Category Parent and idCategory is same.");
            }

            Optional<Category> categoryParent = categoryService.findCategoryById(updateCategoryRequest.getParentId());
            if (categoryParent.isEmpty()) {
                log.trace("Category Parent not exist " + updateCategoryRequest);
                return ResponseEntity.status(409).body("Category Parent not Exists.");
            }

            if (categoryParent.get().getParentId() != null){
                log.trace("Category Parent is subcategory " + updateCategoryRequest);
                return ResponseEntity.status(409).body("The parent category is a subcategory. You cannot create more levels.");
            }

            if (!categoryService.findCategoryByParentId(idCategory).isEmpty()){
                log.trace("Category to edit have subcategory. " + updateCategoryRequest);
                return ResponseEntity.status(409).body("Category to edit have subcategory.");
            }
        }

        log.trace("Update client " + updateCategoryRequest);

        Category updateCategory = existingCategory.get();
        updateCategory.setName(updateCategoryRequest.getName());
        updateCategory.setDescription(updateCategoryRequest.getDescription());
        updateCategory.setParentId(updateCategoryRequest.getParentId());

        categoryService.createCategory(updateCategory);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idCategory)
                .toUri();
        return ResponseEntity.created(uri).body(idCategory);
    }

    @DeleteMapping("/{idCategory}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long idCategory){
        log.trace("deleteCategory");

        if (categoryService.findCategoryById(idCategory).isEmpty()) {
            log.trace("Category to deleted not exist");
            return ResponseEntity.status(409).body("Category not Exists.");
        }
        List<Category> existingCategory = categoryService.findCategoryByParentId(idCategory).stream().toList();

        if (!existingCategory.isEmpty()) {
            log.trace("Category have childs");
            return ResponseEntity.status(409).body("Category have childs.");
        }

        if (categoryService.removeCategory(idCategory)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).body("Could not be removed. There are products associated with this category.");
        }
    }

}
