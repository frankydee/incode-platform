package org.incode.domainapp.example.dom.dom.classification.fixture;

import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.apache.isis.applib.fixturescripts.DiscoverableFixtureScript;
import org.apache.isis.applib.services.factory.FactoryService;

import org.incode.domainapp.example.dom.demo.dom.demowithatpath.DemoObjectWithAtPath;
import org.incode.domainapp.example.dom.dom.classification.dom.classification.demowithatpath.ClassificationForDemoObjectWithAtPath_classify;
import org.incode.domainapp.example.dom.demo.dom.otherwithatpath.OtherObjectWithAtPath;
import org.incode.module.classification.dom.impl.category.Category;
import org.incode.module.classification.dom.impl.category.CategoryRepository;
import org.incode.module.classification.dom.impl.category.taxonomy.Taxonomy;
import org.incode.module.classification.dom.impl.classification.T_classify;
import org.incode.domainapp.example.dom.demo.dom.demowithatpath.DemoObjectWithAtPathMenu;
import org.incode.domainapp.example.dom.demo.dom.otherwithatpath.OtherObjectWithAtPathMenu;

public class ClassifiedDemoObjectsFixture extends DiscoverableFixtureScript {

    //region > constructor
    public ClassifiedDemoObjectsFixture() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }
    //endregion

    //region > mixins
    T_classify classify(final Object classifiable) {
        return mixin(ClassificationForDemoObjectWithAtPath_classify.class, classifiable);
    }
    //endregion

    //region > demoObjects (output)
    private List<DemoObjectWithAtPath> demoObjects = Lists.newArrayList();

    public List<DemoObjectWithAtPath> getDemoObjects() {
        return demoObjects;
    }
    //endregion

    //region > otherObjects (output)
    private List<OtherObjectWithAtPath> otherObjects = Lists.newArrayList();

    public List<OtherObjectWithAtPath> getOtherObjects() {
        return otherObjects;
    }
    //endregion

    @Override
    protected void execute(final ExecutionContext executionContext) {
        // prereqs
        executionContext.executeChild(this, new ClassificationDemoAppTearDownFixture());

        // italian taxonomy applicable only to Italian DemoObject and OtherObject
        Taxonomy italianColours = categoryRepository.createTaxonomy("Italian Colours");
        italianColours.setReference("ITACOL");
        Category italianRed = italianColours.addChild("Red", "RED", null);
        Category italianGreen = italianColours.addChild("Green", "GREEN", null);
        Category italianWhite = italianColours.addChild("White", "WHITE", null);

        wrap(italianColours).applicable("/ITA", DemoObjectWithAtPath.class.getName());
        wrap(italianColours).applicable("/ITA", OtherObjectWithAtPath.class.getName());

        // french taxonomy applicable only to French DemoObject (and not to OtherObject even if with FRA app tenancy)
        Taxonomy frenchColours = categoryRepository.createTaxonomy("French Colours");
        frenchColours.setReference("FRCOL");
        Category frenchRed = frenchColours.addChild("Red", "FRRED", null);
        Category frenchWhite = frenchColours.addChild("White", "FRWHITE", null);
        Category frenchBlue = frenchColours.addChild("Blue", "FRBLUE", null);

        wrap(frenchColours).applicable("/FRA", DemoObjectWithAtPath.class.getName());

        // global taxonomy applicable only to DemoObject (any app tenancy)
        Taxonomy globalSizes = categoryRepository.createTaxonomy("Sizes");
        globalSizes.setReference("SIZES");
        Category large = globalSizes.addChild("Large", "LGE", 1);
        Category medium = globalSizes.addChild("Medium", "M", 2);
        Category small = globalSizes.addChild("Small", "SML", 3);

        Category largeLargest = large.addChild("Largest", "XXL", 1);
        Category largeLarger = large.addChild("Larger", "XL", 2);
        Category largeLarge = large.addChild("Large", "L", 3);

        Category smallSmall = small.addChild("Small", "S", 1);
        Category smallSmaller = small.addChild("Smaller", "XS", 2);
        Category smallSmallest = small.addChild("Smallest", "XXS", 3);

        wrap(globalSizes).applicable("/", DemoObjectWithAtPath.class.getName());

        // create a sample set of DemoObject and OtherObject, for various app tenancies

        final DemoObjectWithAtPath demoFooInItaly = createDemo("Demo foo (in Italy)", "/ITA", executionContext);
        final DemoObjectWithAtPath demoBarInFrance = createDemo("Demo bar (in France)", "/FRA", executionContext);
        final DemoObjectWithAtPath demoBaz = createDemo("Demo baz (Global)", "/", executionContext);
        final DemoObjectWithAtPath demoBip = createDemo("Demo bip (in Milan)", "/ITA/I-MIL", executionContext);
        final DemoObjectWithAtPath demoBop = createDemo("Demo bop (in Paris)", "/FRA/F-PAR", executionContext);

        final OtherObjectWithAtPath otherFooInItaly = createOther("Other foo (in Italy)", "/ITA", executionContext);

        final OtherObjectWithAtPath otherBarInFrance = createOther("Other bar (in France)", "/FRA", executionContext);
        final OtherObjectWithAtPath otherBaz = createOther("Other baz (Global)", "/", executionContext);
        final OtherObjectWithAtPath otherBip = createOther("Other bip (in Milan)", "/ITA/I-MIL", executionContext);
        final OtherObjectWithAtPath otherBop = createOther("Other bop (in Paris)", "/FRA/F-PAR", executionContext);

        // classify DemoObject

        final ClassificationForDemoObjectWithAtPath_classify mixinFrance = factoryService.mixin(ClassificationForDemoObjectWithAtPath_classify.class, demoBarInFrance);
        final ClassificationForDemoObjectWithAtPath_classify mixin = factoryService.mixin(ClassificationForDemoObjectWithAtPath_classify.class, demoFooInItaly);
        final ClassificationForDemoObjectWithAtPath_classify mixin1 = factoryService.mixin(ClassificationForDemoObjectWithAtPath_classify.class, demoFooInItaly);
        wrap(mixin).classify(italianColours, italianRed);

        wrap(mixin1).classify(globalSizes, medium);

        wrap(mixinFrance).classify(globalSizes, smallSmaller);

        // leave OtherObject unclassified

    }

    private DemoObjectWithAtPath createDemo(
            final String name,
            final String atPath,
            final ExecutionContext executionContext) {
        final DemoObjectWithAtPath demoObject = wrap(demoObjectMenu).create(name, atPath);
        demoObjects.add(demoObject);
        return executionContext.addResult(this, demoObject);
    }

    private OtherObjectWithAtPath createOther(
            final String name,
            final String atPath,
            final ExecutionContext executionContext) {
        final OtherObjectWithAtPath otherObject = wrap(otherObjectMenu).create(name, atPath);
        otherObjects.add(otherObject);
        return executionContext.addResult(this, otherObject);
    }

    //region > injected services
    @javax.inject.Inject
    DemoObjectWithAtPathMenu demoObjectMenu;
    @javax.inject.Inject
    OtherObjectWithAtPathMenu otherObjectMenu;
    @javax.inject.Inject
    CategoryRepository categoryRepository;
    //endregion

    @Inject
    FactoryService factoryService;

}
