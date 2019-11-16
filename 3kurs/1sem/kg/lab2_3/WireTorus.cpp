#include <GL/glut.h>
#include "FigureTemplate.cpp"

class WireTorus : public FigureTemplate {
public:
    explicit WireTorus(int *listId) : FigureTemplate(listId) {}


private:
    void drawSpecificFigure(int figureParams[]) override {

        glutWireTorus(figureParams[0], figureParams[1], figureParams[2], figureParams[3]);

    }
};