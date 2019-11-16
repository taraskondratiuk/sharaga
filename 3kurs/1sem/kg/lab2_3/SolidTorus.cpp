#include <GL/glut.h>
#include "FigureTemplate.cpp"


class SolidTorus : public FigureTemplate {
public:

    explicit SolidTorus(int *listId) : FigureTemplate(listId) {}

private:
    void drawSpecificFigure(int figureParams[]) override {

        glutSolidTorus(figureParams[0], figureParams[1], figureParams[2], figureParams[3]);
    }
};