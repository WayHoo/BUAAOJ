package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Language;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageMapper {
    Language getLanguageById(int languageId);
    List<Language> getAllLanguages();
}
