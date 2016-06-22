'use strict';

describe('Controller Tests', function() {

    describe('Recommander Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRecommander, MockOuvrage, MockUtilisateur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRecommander = jasmine.createSpy('MockRecommander');
            MockOuvrage = jasmine.createSpy('MockOuvrage');
            MockUtilisateur = jasmine.createSpy('MockUtilisateur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Recommander': MockRecommander,
                'Ouvrage': MockOuvrage,
                'Utilisateur': MockUtilisateur
            };
            createController = function() {
                $injector.get('$controller')("RecommanderDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:recommanderUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
