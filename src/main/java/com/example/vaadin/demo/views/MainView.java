package com.example.vaadin.demo.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;


@Route
public class MainView extends VerticalLayout {

    private Grid<Test> grid;
    TextField filter;
    private Button addNewBtn;

    private final TestRepository testRepository;

    private final TestEditor testEditor;

    public MainView(TestRepository testRepository, TestEditor testEditor) {
        this.testRepository = testRepository;
        this.testEditor = testEditor;
        this.grid = new Grid<>(Test.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("New test", VaadinIcon.PLUS.create());
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid, testEditor);

        grid.setHeight("200px");
        grid.setColumns("id", "data1", "data2");
        grid.getColumnByKey("id").setWidth("60px").setFlexGrow(0);

        filter.setPlaceholder("Filter by last name");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listEmployees(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            testEditor.editEmployee(e.getValue());
        });

        addNewBtn.addClickListener(e -> testEditor.editEmployee(new Test()));

        testEditor.setChangeHandler(() -> {
            testEditor.setVisible(false);
            listEmployees(filter.getValue());
        });

        listEmployees(null);
    }

    void listEmployees(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(testRepository.getTests());
        } else {
            grid.setItems(testRepository.findData1StartWith(filterText));
        }
    }
}
