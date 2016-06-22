'use strict';

describe('Controller Tests', function() {

    describe('Noter Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockNoter, MockOuvrage, MockUtilisateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockNoter = jasmine.createSpy('MockNoter');
            MockOuvrage = jasmine.createSpy('MockOuvrage');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Noter': MockNoter,
                'Ouvrage': MockOuvrage,
                'Utilisateur': MockUtilisateur
            };
            createController = function() {
                $injector.get('$controller')("NoterDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:noterUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
