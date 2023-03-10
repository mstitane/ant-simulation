package ch.epfl.moocprog.gfx;

import ch.epfl.moocprog.Animal;
import ch.epfl.moocprog.AnimalVisitor;
import ch.epfl.moocprog.AntSoldier;
import ch.epfl.moocprog.AntWorker;
import ch.epfl.moocprog.RenderingMedia;
import ch.epfl.moocprog.Termite;

final class AnimalsRenderer implements AnimalVisitor {
    private final AntWorkerRenderer antWorkerRenderer;
    private final AntSoldierRenderer antSoldierRenderer;
    private final TermiteRenderer termiteRenderer;

    AnimalsRenderer() {
        this.antWorkerRenderer = new AntWorkerRenderer();
        this.antSoldierRenderer = new AntSoldierRenderer();
        this.termiteRenderer = new TermiteRenderer();
    }

    @Override
    public void visit(AntWorker antWorker, RenderingMedia canvas) {
        antWorkerRenderer.render((JavaFXAntSimulationCanvas)canvas, antWorker);
    }

    @Override
    public void visit(AntSoldier antSoldier, RenderingMedia canvas) {
        antSoldierRenderer.render((JavaFXAntSimulationCanvas)canvas, antSoldier);
    }

    @Override
    public void visit(Termite termite, RenderingMedia canvas) {
        termiteRenderer.render((JavaFXAntSimulationCanvas)canvas, termite);
    }

    void render(JavaFXAntSimulationCanvas canvas, Animal animal) {
        animal.accept(this, canvas);
    }
}
