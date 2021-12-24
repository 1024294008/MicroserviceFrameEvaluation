package cn.hp.service;

import cn.hp.entity.Module;

import java.io.File;
import java.util.List;

public interface IPomParseService {
    Module parseModule(File pomFile);

    List<String> parseDependencyList(File pomFile);
}
