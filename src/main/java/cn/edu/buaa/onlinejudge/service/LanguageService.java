package cn.edu.buaa.onlinejudge.service;

import cn.edu.buaa.onlinejudge.mapper.LanguageMapper;
import cn.edu.buaa.onlinejudge.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {
    @Autowired
    private LanguageMapper languageMapper;

    public Language getLanguageById(int languageId) {
        return languageMapper.getLanguageById(languageId);
    }

    public List<Language> getAllLanguages() {
        return languageMapper.getAllLanguages();
    }
}
