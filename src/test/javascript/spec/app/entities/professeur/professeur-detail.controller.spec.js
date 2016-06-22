'use strict';

describe('Controller Tests', function() {

    describe('Professeur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockProfesseur, MockUtilisateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockProfesseur = jasmine.createSpy('MockProfesseur');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Professeur': MockProfesseur,
                'Utilisateur': MockUtilisateur
            };
            createController = function() {
                $injector.get('$controller')("ProfesseurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:professeurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
