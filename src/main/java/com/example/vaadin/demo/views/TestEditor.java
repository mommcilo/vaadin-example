package com.example.vaadin.demo.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SpringComponent
@UIScope
public class TestEditor extends VerticalLayout implements KeyNotifier {

    private TestRepository testRepository;
    private Test test;

    TextField data1 = new TextField("Data 1");
    TextField data2 = new TextField("Data 2");

    Button save = new Button("Save", VaadinIcon.CHECK.create());
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete", VaadinIcon.TRASH.create());

    HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    Binder<Test> binder = new Binder<>(Test.class);
    private ChangeHandler changeHandler;

    @Autowired
    public TestEditor(TestRepository repository) {
        this.testRepository = repository;

        add(data1, data2, actions);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmployee(test));
        setVisible(false);
    }

    void delete() {
        testRepository.removeTest(test);
        changeHandler.onChange();
    }

    void save() {
        test.generateId();
        testRepository.addTest(test);
        changeHandler.onChange();
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editEmployee(Test c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            List<Test> byId = testRepository.findById(c.getId());
            test = byId.get(0);
        } else {
            test = c;
        }

        cancel.setVisible(persisted);
        binder.setBean(test);
        setVisible(true);
        data1.focus();
    }

    public void setChangeHandler(ChangeHandler h) {
        changeHandler = h;
    }
}
