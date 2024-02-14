package xyz.me4cxy.proxy.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jayin
 * @since 2024/01/29
 */
public abstract class SpringConfigurerSupport implements ApplicationContextAware {
    private ApplicationContext context;
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    public <T> T autowiredAndInitializeBean(T bean) {
        autowireCapableBeanFactory.autowireBean(bean);
        return (T) autowireCapableBeanFactory.initializeBean(bean, bean.getClass().getName());
    }

    public <T> List<T> autowiredAndInitializeBean(List<T> beans) {
        return autowiredAndInitializeBean(beans);
    }

    public <T> List<T> autowiredAndInitializeBean(List<T> beans, Comparator<T> comparator) {
        Stream<T> tStream = beans.stream().map(this::autowiredAndInitializeBean);
        if (comparator != null) {
            tStream = tStream.sorted(comparator);
        }
        return tStream.collect(Collectors.toList());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
        autowireCapableBeanFactory = context.getAutowireCapableBeanFactory();
    }
}
