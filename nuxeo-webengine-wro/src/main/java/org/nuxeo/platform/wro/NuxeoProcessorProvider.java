package org.nuxeo.platform.wro;

import java.util.Map;

import org.nuxeo.platform.wro.processor.LessCssProcessor;

import ro.isdc.wro.extensions.processor.support.DefaultProcessorProvider;
import ro.isdc.wro.model.resource.processor.ResourcePostProcessor;
import ro.isdc.wro.model.resource.processor.ResourcePreProcessor;
import ro.isdc.wro.model.resource.processor.decorator.LazyProcessorDecorator;
import ro.isdc.wro.model.resource.processor.decorator.ProcessorDecorator;
import ro.isdc.wro.util.LazyInitializer;

public class NuxeoProcessorProvider extends DefaultProcessorProvider {

    @Override
    public Map<String, ResourcePreProcessor> providePreProcessors() {
        Map<String, ResourcePreProcessor> preProcessors = super.providePreProcessors();

        preProcessors.put(LessCssProcessor.ALIAS,
                new LazyProcessorDecorator(
                        new LazyInitializer<ResourcePreProcessor>() {
                            @Override
                            protected ResourcePreProcessor initialize() {
                                return new LessCssProcessor();
                            }
                        }));
        return preProcessors;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, ResourcePostProcessor> providePostProcessors() {
        /**
         * Created to overcome the difference between
         * {@link ResourcePreProcessor} and {@link ResourcePostProcessor}
         * interfaces which will be resolved in next major version.
         */
        Map<String, ResourcePostProcessor> postProcessors = super.providePostProcessors();
        postProcessors.put(LessCssProcessor.ALIAS, new ProcessorDecorator(
                new LazyProcessorDecorator(
                        new LazyInitializer<ResourcePreProcessor>() {
                            @Override
                            protected ResourcePreProcessor initialize() {
                                return new LessCssProcessor();
                            }
                        })));

        return postProcessors;

    }
}
