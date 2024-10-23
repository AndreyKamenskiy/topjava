package ru.javawebinar.topjava;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import ru.javawebinar.topjava.repository.UserRepository;

@Component
public class TestBeanPostProcessor implements BeanFactoryPostProcessor {

    ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        setNotAutowire(UserRepository.class, "inmemoryUserRepository");
    }

    private void setNotAutowire(Class<?> clazz, String correctBeanId) {
        String[] repoNames = beanFactory.getBeanNamesForType(clazz);
        for (String beanName : repoNames) {
            if (!correctBeanId.equals(beanName)) {
                BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
                bd.setAutowireCandidate(false);
            }
        }
    }
}