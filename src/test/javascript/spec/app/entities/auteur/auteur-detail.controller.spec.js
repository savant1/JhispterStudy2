'use strict';

describe('Controller Tests', function() {

    describe('Auteur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAuteur, MockRealiser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAuteur = jasmine.createSpy('MockAuteur');
            MockRealiser = jasmine.createSpy('MockRealiser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Auteur': MockAuteur,
                'Realiser': MockRealiser
            };
            createController = function() {
                $injector.get('$controller')("AuteurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'projetApp:auteurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
