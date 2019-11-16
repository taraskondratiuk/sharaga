#include <GL/glut.h>
#include "FigureTemplate.cpp"


class WireCube : public FigureTemplate {
public:
    explicit WireCube(int *listId) : FigureTemplate(listId) {}



private:
    void drawSpecificFigure(int figureParams[]) override {
        glutWireCube(figureParams[0]);
    }
};
