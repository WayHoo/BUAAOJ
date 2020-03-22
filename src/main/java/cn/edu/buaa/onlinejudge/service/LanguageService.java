package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.LanguageMapper;
import cn.edu.buaa.onlinejudge.model.Language;
import org.springframework.stereotype.Service;

@Service
public class LanguageService {

    private final LanguageMapper languageMapper;

    public LanguageService(LanguageMapper languageMapper) {
        this.languageMapper = languageMapper;
    }

    public Language getLanguageById(int languageId) {
        return languageMapper.getLanguageById(languageId);
    }
}
