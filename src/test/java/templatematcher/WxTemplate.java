package templatematcher;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value @Builder
public class WxTemplate {
    @Singular
    private List<WxLine> wxLines;
}
