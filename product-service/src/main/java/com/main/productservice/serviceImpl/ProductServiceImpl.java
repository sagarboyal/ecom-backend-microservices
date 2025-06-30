package com.main.productservice.serviceImpl;

import com.main.productservice.entity.Category;
import com.main.productservice.entity.Product;
import com.main.productservice.exceptions.custom.APIException;
import com.main.productservice.exceptions.custom.ResourceNotFoundException;
import com.main.productservice.payloads.request.ProductRequest;
import com.main.productservice.payloads.request.ProductUpdateRequest;
import com.main.productservice.payloads.response.PageResponse;
import com.main.productservice.payloads.response.ProductResponse;
import com.main.productservice.repository.CategoryRepository;
import com.main.productservice.repository.ProductRepository;
import com.main.productservice.service.FileService;
import com.main.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final FileService fileService;

    @Value("${project.images}")
    private String path;

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        Category category = getCategoryById(productRequest.getCategoryId());

        boolean isPresent = productRepository.existsByProductNameEqualsIgnoreCase(productRequest.getProductName());

        if(isPresent)
            throw new APIException("Product " + productRequest.getProductName() + " already exists");

        Product product = convertProduct(productRequest);
        product.setCategoryId(category.getCategoryId());

        double discountPercent = product.getDiscount() != null ? product.getDiscount() : 0.0;
        double price = product.getPrice() != null ? product.getPrice() : 0.0;

        double discountAmount = (discountPercent * 0.01) * price;
        double specialPrice = Math.max(price - discountAmount, 0.0);
        specialPrice = Math.round(specialPrice * 100.0) / 100.0;

        product.setSpecialPrice(specialPrice);
        product.setImage("images/products/default.jpg");

        Product savedProduct = productRepository.save(product);
        return convertResponse(savedProduct, category.getCategoryName(), "");
    }
    @Override
    public PageResponse<ProductResponse> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        return getPagedResponse(productPage);
    }



    @Override
    public PageResponse<ProductResponse> getAllProductsByCategoryId(String categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = getCategoryById(categoryId);

        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAllByCategoryId(category.getCategoryId(), pageDetails);

        return getPagedResponse(productPage);
    }

    @Override
    public PageResponse<ProductResponse> getAllProductsByCategoryKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase(keyword, pageDetails);

        return getPagedResponse(productPage);
    }




    @Override
    public ProductResponse updateProduct(ProductUpdateRequest productRequest) {
        Product savedProduct = getProductById(productRequest.getProductId());

        String newProductName = productRequest.getProductName();
        if (newProductName != null && !newProductName.trim().isEmpty()) {
            if (productRepository.existsByProductNameEqualsIgnoreCase(newProductName)) {
                throw new APIException("Product '" + newProductName + "' already exists");
            }
            savedProduct.setProductName(newProductName);
        }

        savedProduct.setDescription(
                productRequest.getDescription() != null && !productRequest.getDescription().trim().isEmpty()
                        ? productRequest.getDescription()
                        : savedProduct.getDescription()
        );

        savedProduct.setQuantity(
                productRequest.getQuantity() != null
                        ? productRequest.getQuantity()
                        : savedProduct.getQuantity()
        );

        savedProduct.setPrice(
                productRequest.getPrice() != null
                        ? productRequest.getPrice()
                        : savedProduct.getPrice()
        );

        savedProduct.setDiscount(
                productRequest.getDiscount() != null
                        ? productRequest.getDiscount()
                        : savedProduct.getDiscount()
        );


        savedProduct.setCategoryId(
                productRequest.getCategoryId() != null && !productRequest.getCategoryId().trim().isEmpty()
                        ? productRequest.getCategoryId()
                        : savedProduct.getCategoryId()
        );

        savedProduct.setSellerId(
                productRequest.getSellerId() != null && !productRequest.getSellerId().trim().isEmpty()
                        ? productRequest.getSellerId()
                        : savedProduct.getSellerId()
        );

        Double newPrice = productRequest.getPrice();
        Double newDiscount = productRequest.getDiscount();

        if (newPrice != null) savedProduct.setPrice(newPrice);
        if (newDiscount != null) savedProduct.setDiscount(newDiscount);


        if (newPrice != null || newDiscount != null) {
            double price = savedProduct.getPrice() != null ? savedProduct.getPrice() : 0.0;
            double discountPercent = savedProduct.getDiscount() != null ? savedProduct.getDiscount() : 0.0;

            double discountAmount = (discountPercent * 0.01) * price;
            double specialPrice = Math.max(price - discountAmount, 0.0);
            specialPrice = Math.round(specialPrice * 100.0) / 100.0;

            savedProduct.setSpecialPrice(specialPrice);
        }

        Product finalSavedProduct = productRepository.save(savedProduct);
        Category category = getCategoryById(finalSavedProduct.getCategoryId());
        return convertResponse(finalSavedProduct, category.getCategoryName(), "");
    }

    @Override
    public ProductResponse updateProductImage(String productId, MultipartFile image) throws IOException {
        Product product = getProductById(productId);
        Category category = getCategoryById(product.getCategoryId());
        String fileName = fileService.uploadImage(path, image);
        product.setImage(fileName);
        product =  productRepository.save(product);
        return convertResponse(product, category.getCategoryName(), "");
    }

    @Override
    public ProductResponse deleteProduct(String productId) {
        Product product = getProductById(productId);
        Category category = getCategoryById(product.getCategoryId());
        productRepository.delete(product);
        return convertResponse(product, category.getCategoryName(), "");
    }

    private Category getCategoryById(String categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
    }
    private Product getProductById(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));
    }
    private ProductResponse convertResponse(Product  product, String categoryName, String sellerName) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .specialPrice(product.getSpecialPrice())
                .image(product.getImage())
                .categoryName(categoryName)
                .sellerName(sellerName)
                .build();
    }
    private Product convertProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setDescription(productRequest.getDescription());
        product.setQuantity(productRequest.getQuantity());
        product.setPrice(productRequest.getPrice());
        product.setDiscount(productRequest.getDiscount());
        product.setCategoryId(productRequest.getCategoryId());
        product.setSellerId(productRequest.getSellerId());
        return product;
    }
    private PageResponse<ProductResponse> getPagedResponse(Page<Product> productPage) {
        List<Product> products = productPage.getContent();

        if (products.isEmpty())
            throw new APIException("No products found!");

        List<ProductResponse> productDTOS = products.stream()
                .map(product -> {
                    Category category = categoryRepository.findById(product.getCategoryId())
                            .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", product.getCategoryId()));
                    return convertResponse(product, category.getCategoryName(), "");
                })
                .toList();

        return PageResponse.<ProductResponse>builder()
                .content(productDTOS)
                .pageNumber(productPage.getNumber())
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .lastPage(productPage.isLast())
                .build();
    }
}
