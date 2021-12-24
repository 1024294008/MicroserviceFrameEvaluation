package cn.hp.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@Data
@NoArgsConstructor
public class ProjectInfo {
    private File projectFile;
}
