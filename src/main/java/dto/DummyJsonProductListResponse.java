package dto;
import java.util.List;

public record DummyJsonProductListResponse(
        List<DummyJsonProduct> products,
        Integer total,
        Integer skip,
        Integer limit
) {
}
