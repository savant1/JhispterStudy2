'use strict';

describe('Controller Tests', function() {

    describe('Emprunter Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEmprunter, MockOuvrage, MockUtilisateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEmprunter = jasmine.createSpy('MockEmprunter');
            MockOuvrage = jasmine.createSpy('MockOuvrage');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Emprunter': MockEmprunter,
                'Ouvrage': MockOuvrage,
                'Utilisateur': MockUtilisateur
            };
            createController = function() {
                $injector.get('$controller')("EmprunterDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:emprunterUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
