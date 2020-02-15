package cn.edu.buaa.onlinejudge.mapper;

import cn.edu.buaa.onlinejudge.model.Language;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageMapper {
    /**
     * 根据编程语言ID获取编程语言对象
     * @param languageId - 编程语言ID
     * @return Language对象
     */
    Language getLanguageById(int languageId);

    /**
     * 获取所有编程语言对象
     * @return Language对象列表
     */
    List<Language> getAllLanguages();
}
