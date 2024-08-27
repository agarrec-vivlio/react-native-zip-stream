require 'json'

package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name         = package['name']
  s.version      = package['version']
  s.summary      = package['description']
  s.homepage     = 'https://github.com/agarrec-vivlio/react-native-zip-stream'
  s.license      = { :type => 'MIT', :file => 'LICENSE' }
  s.author       = { 'Your Name' => 'your-email@example.com' }
  s.source       = { :git => 'https://github.com/agarrec-vivlio/react-native-zip-stream.git', :tag => s.version.to_s }
  s.source_files  = 'ios/**/*.{h,m,swift}'
  s.requires_arc = true
  s.dependency 'React'
  s.dependency 'ZipArchive'
end