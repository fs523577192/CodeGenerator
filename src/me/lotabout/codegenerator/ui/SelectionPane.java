package me.lotabout.codegenerator.ui;

import com.intellij.openapi.ui.Messages;
import me.lotabout.codegenerator.config.ClassSelectionConfig;
import me.lotabout.codegenerator.config.MemberSelectionConfig;
import me.lotabout.codegenerator.config.PipelineStep;

import javax.swing.*;
import java.awt.*;

public class SelectionPane implements PipelineStepConfig {
    private JTextField postfixText;
    private JCheckBox enableStepCheckBox;
    private JButton moveTabLeftButton;
    private JButton moveTabRightButton;
    private JButton removeThisStepButton;
    private JPanel topPanel;
    private JScrollPane contentPane;

    private Object selectionPane;

    public SelectionPane(PipelineStep config, TemplateEditPane parent) {
        postfixText.setText(config.postfix());
        enableStepCheckBox.setSelected(config.enabled());

        removeThisStepButton.addActionListener(e -> {
            int result = Messages.showYesNoDialog("Really remove this step?", "Delete", null);
            if (result == Messages.OK) {
                parent.removePipelineStep(this);
            }
        });

        if (config instanceof MemberSelectionConfig) {
            MemberSelectionPane pane = new MemberSelectionPane((MemberSelectionConfig)config);
            contentPane.setViewportView(pane.getComponent());
            selectionPane = pane;
        } else if (config instanceof ClassSelectionConfig) {
            ClassSelectionPane pane = new ClassSelectionPane((ClassSelectionConfig)config);
            contentPane.setViewportView(pane.getComponent());
        }
    }

    public String postfix() {
        return postfixText.getText();
    }

    public boolean enabled() {
        return enableStepCheckBox.isSelected();
    }

    @Override public PipelineStep getConfig() {
        if (selectionPane instanceof MemberSelectionPane) {
            PipelineStep step = ((MemberSelectionPane)selectionPane).getConfig();
            step.postfix(this.postfix());
            step.enabled(this.enabled());
            return step;
        } else if (selectionPane instanceof ClassSelectionPane) {
            PipelineStep step = ((ClassSelectionPane)selectionPane).getConfig();
            step.postfix(this.postfix());
            step.enabled(this.enabled());
            return step;
        }
        return null;
    }

    @Override public JComponent getComponent() {
        return topPanel;
    }
}
